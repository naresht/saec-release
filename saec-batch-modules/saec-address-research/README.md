# Overview
Experimental Design to build SAEC Features as 'modular' development. All services, batch, dto, schema, mock vendor services 
(info age), events, errors, tests, and test data will be self-contained in THIS module.
   
# GOALS (Benefits)
* Separation of Business Concerns over Technical Concerns
* Consistent, Meaningful naming convention that reflects business processes
* Easy to test and deploy as a new 'update' or a feature without affecting rest of the modules
* Promotes code ownership, easier to understand business functions (central Vs scattered among several modules). 
* Transparent requirements traceability in code.
* Functional back end code with unit/mock and integration tests.
* Ideally, there should be NO business logic in saec-ui module; only jsf facelets and spring webflow config.
* Open for extension or changes of internal design in the future. For instance, Spring batch can be replaced
  by Akka Event Driven Architecture for highly available service. Or services may be implemented as 
  Camel Services. All custom framework 'infrastructure' code will lie in THIS module as long as it
  adheres to Spring Container, JPA, and Spring Roo domain model -- the least common denominator of this framework. 
* Encourages design optimal for each module rather than a "one size fits all" monolithic model. A good example is the 
  rules pkg, which at this moment is open for multiple implementions.     
* NO dependencies on any other module except saec-domain and saec-infrastructure. 
  
  WHAT DOES THIS MEAN? 
  
  It means you can code whatever you want but it won't break any other module. This is the biggest challenge in a software
  or product devlopment life cycle. It deserves more attention than anything else, architecturally, since now, all we 
  do is wire spring breans, bundle maven repops, 'adhere' to spring batch interfaces and debug jsf horror stories and IE7 issues.
  Without any emphasis in Business Requirements, the code can quickly transform into spaghetti. 
  
  Put simply -- the more dependencies each module has on other modules, the less stable the system is and the more harder it is to
  understand, refactor, test, evolve, and maintain over time.
  
  
# TODO
* Mock Vendor Implementation of infoage; useful to test end to end functional testing when
  Vendor integration is not available.
* Performance Testing of Spring Batch / JaxB conversions for 1 million records  
  
    
