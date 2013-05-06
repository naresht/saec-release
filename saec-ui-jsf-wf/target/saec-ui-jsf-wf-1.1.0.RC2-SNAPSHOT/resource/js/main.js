//-------------------------------------
// Invoke automatically the quit action
// Used to propagate the quit action to all the open flows.
//-------------------------------------
jQuery(function() {
	if (document.URL.indexOf('_cascadeQuit', 0) > 0) {
		APP.menu.quit('form');
	}
});

//-------------------------------------
// Menu shortcuts
//-------------------------------------
APP = {};
APP.menu = {};
APP.menu.call = function(idToClick) {
  jQuery(PrimeFaces.escapeClientId(idToClick)).click();	
  return false;
};
APP.menu.open = function(form) {
  return APP.menu.call(form + ':open');
};
APP.menu.createNew = function(form) {
  return APP.menu.call(form + ':createNew');
};
APP.menu.save = function(form) {
  return APP.menu.call(form + ':save');
};
APP.menu.saveAndClose = function(form) {
  return APP.menu.call(form + ':saveAndClose');
};
APP.menu.deleteAndClose = function(form) {
  return APP.menu.call(form + ':deleteAndClose');
};
APP.menu.close = function(form) {
  return APP.menu.call(form + ':close');
};
APP.menu.quit = function(form) {
  return APP.menu.call(form + ':quit');
};
APP.menu.debugThrowException = function(form) {
  return APP.menu.call(form + ':debugThrowException');
};
APP.menu.send = function(form) {
	return APP.menu.call(form + ':send');
};
