export interface User {
  id: number;
  fullname: String;
  userName: string;
  gender: string;
  address: string;
  position: string;
  salary: number;
  telNumber: string;
  hireDate: Date;
  email: string;
  password?: string;
  role: string;
}
