This module should GO once codebase is stable.  

Since daos are needed only on a per use case basis, saec-domain providing 90% of persistence capabilities across saec,
it makes sense to drop this module and include the daos in the individual 'feature' modules. 