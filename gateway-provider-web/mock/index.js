const glob = require('glob');
const { wrap7A } = require('./utils/utils');

const mockFiles = glob.sync('./*.js', {
  cwd: __dirname,
  ignore: ['./index.js']
});

let apis = mockFiles.reduce((memo, mockFile) => {
  const m = require(mockFile);
  memo = {
    ...memo,
    ...(m.default || m)
  };
  return memo;
}, {});

for (let key in apis) {
  if (typeof apis[key] === 'object') {
    let data = wrap7A(apis[key]);
    apis[key] = (req, res) => {
      res.send(data);
    };
  }
}

module.exports = apis;
