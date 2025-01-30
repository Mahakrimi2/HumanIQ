export class AuthModel {
  email: string;
  password: string;
  constructor(email: string, password: string) {
    this.email = email;
    this.password = password;
  }
}

export interface LoginDTO {
  token: string;
}

export interface AuthRequest {
  email: string;
  password: string;
}
