window.fakeStorage = {
  _data: {},

  setItem: function (id, val) {
    return this._data[id] = String(val);
  },

  getItem: function (id) {
    return this._data.hasOwnProperty(id) ? this._data[id] : undefined;
  },

  removeItem: function (id) {
    return delete this._data[id];
  },

  clear: function () {
    return this._data = {};
  }
};

function LocalStorageManager() {
  this.bestScoreKey     = "bestScore";
  this.gameStateKey     = "gameState";

  var supported = this.localStorageSupported();
  this.storage = supported ? window.localStorage : window.fakeStorage;
}

LocalStorageManager.prototype.localStorageSupported = function () {
  var testKey = "test";

  try {
    var storage = window.localStorage;
    storage.setItem(testKey, "1");
    storage.removeItem(testKey);
    return true;
  } catch (error) {
    return false;
  }
};

// Best score getters/setters
LocalStorageManager.prototype.getBestScore = function () {
  return this.storage.getItem(this.bestScoreKey) || 0;
};

LocalStorageManager.prototype.setBestScore = function (score) {
  jQuery.ajax({
    type: 'POST',
    url: 'UpdateData',
    data: {
      'score': score
    },
    success: function(data){
      if (data.status === 0) console.log("update success");
      else console.log("update failed");
    },
    dataType: 'json',
    error: function(){
      console.log("update error");
    }
  });
  this.storage.setItem(this.bestScoreKey, score);
};

// Game state getters/setters and clearing
LocalStorageManager.prototype.getGameState = function () {
  var stateJSON = this.storage.getItem(this.gameStateKey);
  return stateJSON ? JSON.parse(stateJSON) : null;
};

LocalStorageManager.prototype.setGameState = function (gameState) {
  //upload to server
  jQuery.ajax({
    type: 'POST',
    url: 'UpdateData',
    data: {
      'status': JSON.stringify(gameState)
    },
    success: function(data){
      if (data.status === 0) console.log("update success");
      else console.log("update failed");
    },
    dataType: 'json',
    error: function(){
      console.log("update error");
    }
  });
  this.storage.setItem(this.gameStateKey, JSON.stringify(gameState));
};
LocalStorageManager.prototype.clearGameState = function () {
  this.storage.removeItem(this.gameStateKey);
};
