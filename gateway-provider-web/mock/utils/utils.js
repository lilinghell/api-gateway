module.exports = {
  wrap7A: function(obj) {
    return {
      code: '000000',
      msg: '成功',
      data: obj
    };
  },
  wrapError: function(obj) {
    return {
      code: obj.code || 'not6A',
      msg: obj.msg || 'error',
      data: obj
    };
  }
};
