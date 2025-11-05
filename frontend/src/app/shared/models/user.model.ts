export interface User {
  id: number;
  name: string;
  email: string;
  phone: string;
  role: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  role?: string;
}

export interface UserUpdateRequest {
  name?: string;
  email?: string;
  phone?: string;
  role?: string;
  password?: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  user: User;
}
