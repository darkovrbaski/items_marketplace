import { Component, OnInit } from '@angular/core';
import { emptyUser, User } from '@app/model';
import { AuthService, ImageService, UserService } from '@app/service';
import { ToastrService } from 'ngx-toastr';

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

  onImageUpload(event: Event) {
    const target = event.target as HTMLInputElement;
    const file = target?.files?.[0] as File;
    const reader = new FileReader();

    reader.addEventListener('load', (event: ProgressEvent<FileReader>) => {
      this.updateUser.image = event.target?.result as string;
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
