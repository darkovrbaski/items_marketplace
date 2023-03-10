import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { emptyUser, User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';
import { ImageService } from 'src/app/service/image.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  user: User | null = emptyUser;
  updateUser: User = emptyUser;
  repeatPassword = '';
  edit = false;
  uploadedImage: File | null = null;
  loading = false;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private toastr: ToastrService,
    private imageService: ImageService
  ) {}

  ngOnInit() {
    this.getUser();
  }

  getUser() {
    this.authService.user.subscribe(user => {
      if (user === null) {
        return;
      }
      this.user = user;
      this.updateUser = { ...this.user };
    });
  }

  enableEdit() {
    this.edit = true;
  }

  cancelEdit() {
    this.edit = false;
    if (this.user) {
      this.updateUser = { ...this.user };
    }
  }

  updateProfile() {
    this.userService.updateUser(this.updateUser).subscribe({
      next: user => {
        this.user = user;
        this.cancelEdit();
        this.toastr.success('Profile updated');
      },
      error: error => {
        let errors = '';
        error.error.errors.forEach((message: string) => {
          errors += `${message}</br>`;
        });
        this.toastr.error(errors, error.error.message, {
          enableHtml: true,
        });
      },
    });
  }

  updatePassword() {
    if (this.updateUser.password !== this.repeatPassword) {
      this.toastr.error('Passwords do not match');
      return;
    }
    this.cancelEdit();
    this.updateUser.password = this.repeatPassword;
    this.userService.updateUser(this.updateUser).subscribe({
      complete: () => {
        this.toastr.success('Password updated');
        this.updateUser.password = '';
        this.repeatPassword = '';
      },
      error: error => {
        let errors = '';
        error.error.errors.forEach((message: string) => {
          errors += `${message}</br>`;
        });
        this.toastr.error(errors, error.error.message, {
          enableHtml: true,
        });
      },
    });
  }

  onImageUpload(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {
      this.updateUser.image = event.target.result;
      this.uploadedImage = file;
    });

    reader.readAsDataURL(file);
  }

  imageUploadAction() {
    if (this.uploadedImage === null) {
      return;
    }
    const imageFormData = new FormData();
    imageFormData.append('image', this.uploadedImage, this.uploadedImage.name);
    this.loading = true;

    this.imageService.uploadUserImage(imageFormData).subscribe({
      complete: () => {
        this.toastr.success('Image updated');
        this.uploadedImage = null;
        this.loading = false;
      },
      error: error => {
        let errors = '';
        error.error.errors.forEach((message: string) => {
          errors += `${message}</br>`;
        });
        this.toastr.error(errors, error.error.message, {
          enableHtml: true,
        });
        if (this.user) {
          this.updateUser.image = this.user.image;
        }
        this.uploadedImage = null;
        this.loading = false;
      },
    });
  }
}
