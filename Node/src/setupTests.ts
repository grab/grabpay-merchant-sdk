import 'jest-date-mock';

// The console.error and console.warn spies for the current test.
// let consoleError = null;
// let consoleWarn = null;
beforeEach(() => {
  // Store the console.error and console.warn spies so we can access them later.
  // consoleError = jest.spyOn(console, 'error');
  // consoleWarn = jest.spyOn(console, 'warn');

  // Clear mock states between tests.
  jest.clearAllMocks();
});

// Fail tests if console.error or console.warn is called.
afterEach(() => {
  // expect(consoleError).toHaveBeenCalledTimes(0);
  // expect(consoleWarn).toHaveBeenCalledTimes(0);
});
