package com.bfds.saec.ui.tests.performance;

import com.bfds.saec.ui.test.support.TimeoutThresholdException;
import com.bfds.saec.ui.tests.performance.profiles.StandardUserSession;
import org.junit.Assert;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.IntOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.kohsuke.args4j.ExampleMode.ALL;

/**
 * A simple Performance/Load/Capacity test runner
 *
 * It allows to specify the number of users, session to be executed per user as well as a steady increment of users for capacity testing
 *
 * Usage for load tests:
 *  java com.bfds.saec.ui.tests.performance.PerformanceTestRunner -users 5 -session 10
 * Runs the tests with 5 users (=threads), each performing 10 browsersession scenarios
 *
 * Usage for capacity tests:
 *  java com.bfds.saec.ui.tests.performance.PerformanceTestRunner -users 5 -session 10 -usersIncr=5 -usersMax=50
 * Runs the tests with 5 initial users (=threads), each performing 10 browsersession scenarios.
 * Subsequently the number of users get incremented by 5 until the max of 50 is reached
 *
 * @author: Erich Eichinger
 * @date: 25/01/12
 */
public class PerformanceTestRunner {

    public static void main(String[] args) throws Exception {
        PerformanceTestRunner app = new PerformanceTestRunner();

        CmdLineParser parser = new CmdLineParser(app);

        try {
            parser.parseArgument(args);
            app.run();
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println(String.format("java %s [options...] arguments...", PerformanceTestRunner.class.getName()));
            parser.printUsage(System.err);
            System.err.println();
            System.err.println(String.format("Example: java %s %s", PerformanceTestRunner.class.getName(), parser.printExample(ALL)));
        }
    }

    final Logger log = LoggerFactory.getLogger(getClass());

    @Option(name="-users",handler=IntOptionHandler.class,usage="specify the number of concurrent users to simulate")
    private int users = 1;

    @Option(name="-sessions",handler=IntOptionHandler.class,usage="specify how many session shall be executed per user and iteration")
    private int sessions = 1;

    @Option(name="-usersMax",handler=IntOptionHandler.class,usage="set the maximum number of users")
    private int usersMax = 1;

    @Option(name="-usersIncr",handler=IntOptionHandler.class,usage="set the increment of users after each iteration")
    private int usersIncr = 1;

    public void run() throws Exception {
        List<BrowserSessionFactory> browserProfiles = new ArrayList<BrowserSessionFactory>();
        browserProfiles.add(new BrowserSessionFactory<StandardUserSession>(StandardUserSession.class, 100, 20000));

        usersMax = Math.max(users, usersMax);

        log.info(String.format("PerformanceTestBegin{users=%s,sessions=%s,usersMax=%s,usersIncr=%s}", users, sessions,usersMax, usersIncr));

        boolean hasErrors = false;

        for(int usersForIteration=users;usersForIteration<=usersMax;usersForIteration+=usersIncr){
            log.info(String.format("IterationBegin{usersForIteration=%s}", usersForIteration));
            hasErrors = hasErrors || runIteration(browserProfiles, usersForIteration);
        }

        Assert.assertFalse("Performance Tests had errors", hasErrors);
    }

    private boolean runIteration(List<BrowserSessionFactory> browserProfiles, int usersForIteration) throws Exception {
        int totalSessions = usersForIteration * sessions;

        ExecutorService executor = Executors.newFixedThreadPool(usersForIteration);

        List<Callable<Exception>> sessions = new ArrayList<Callable<Exception>>();

        for(int i=0;i<totalSessions;i++) {
            // TODO: in case we have more than 1 profile, select them based on probability/distribution
            BrowserSessionFactory profile = browserProfiles.get(0);
            Callable<Exception> session = new SessionResultReporter(profile.getProfileClass(), profile.newRunnable());
            sessions.add(session);
        }
        List<Future<Exception>> results = executor.invokeAll(sessions);
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        for(Future<Exception> res:results) {
            if (res.get() != null) {
                return true;
            }
        }
        return false;
    }

    public class SessionResultReporter implements Callable<Exception> {
        private final Class profileClass;
        final Runnable inner;

        public SessionResultReporter(Class profileClass, Runnable inner) {
            this.profileClass = profileClass;
            this.inner = inner;
        }

        @Override
        public Exception call() {
            long startMillis = System.currentTimeMillis();
            Exception ex = null;
            try {
                inner.run();
                return null;
            } catch(Exception e) {
                ex = e;
                return ex;
            } finally {
               double duration = (System.currentTimeMillis()-startMillis)/1000.0;
                String msg = String.format("Session{profile=%s, duration=%1.2fs, isTimeout=%s, isError=%s}", profileClass.getSimpleName(), duration, (ex instanceof TimeoutThresholdException), ex != null);
                log.info(msg, ex);
            }
        }
    }
}
