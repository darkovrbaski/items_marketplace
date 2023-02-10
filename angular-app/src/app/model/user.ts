export interface User {
  id: number;
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  image: string;
  address: {
    country: string;
    city: string;
    street: string;
    number: string;
  };
}

export const emptyUser: User = {
  id: 0,
  username: '',
  password: '',
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  image: '',
  address: {
    country: '',
    city: '',
    street: '',
    number: '',
  },
};
