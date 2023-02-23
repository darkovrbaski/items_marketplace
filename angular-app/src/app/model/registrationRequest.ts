export interface RegistrationRequest {
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
