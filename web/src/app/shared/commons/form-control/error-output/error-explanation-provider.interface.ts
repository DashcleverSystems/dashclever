export interface IErrorExplanationProvider {
  canHandle(error: ErrorNameWithDescription): boolean;

  provideExplanation(error: ErrorNameWithDescription): string;
}

export interface ErrorNameWithDescription {
  name: string;
  description: any;
}
