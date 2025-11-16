export interface ApiResponse<T> {
  data: T;
  errors?: string[];
  fieldErrors?: Record<string, string>;
  message: string;
}