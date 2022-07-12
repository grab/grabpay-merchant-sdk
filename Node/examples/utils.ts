import readline from 'readline';

export const questionAsync = (rl: readline.Interface, question: string) =>
  new Promise<string>((resolve) => {
    rl.question(question, (answer) => resolve(answer));
  });
