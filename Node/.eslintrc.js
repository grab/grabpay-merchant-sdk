const fs = require('fs');
const path = require('path');

const prettierOptions = JSON.parse(fs.readFileSync(path.resolve(__dirname, '.prettierrc'), 'utf8'));
module.exports = {
  extends: ['plugin:prettier/recommended'],
  parser: '@typescript-eslint/parser',
  rules: {
    'arrow-body-style': [2, 'as-needed'],
    'import/no-webpack-loader-syntax': 0,
    'no-case-declarations': 1,
    'no-console': 1,
    'no-param-reassign': 1,
    'prefer-arrow-callback': 'error',
    'prefer-const': 'error',
    'prefer-template': 2,
    'prettier/prettier': ['warn', prettierOptions],
    'require-await': 2,
  },
};
