export type UserStatus =
  | 'PENDING'
  | 'ACTIVE'
  | 'SUSPENDED'
  | 'LOCKED'
  | 'DELETED';

export interface AuthUser {
  id: string;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  status: UserStatus;
}
