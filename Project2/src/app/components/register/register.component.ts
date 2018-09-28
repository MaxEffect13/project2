import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post.service';
import { User } from '../../models/user';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  username: string;
  password: string;
  email: string;

  public usr: User;
  constructor(private postService: PostService) { }

  ngOnInit() {
    this.usr = new User();
  }

  register() {
    this.usr.username = this.username;
    this.usr.password = this.password;
    this.usr.email = this.email;
    this.postService.register(this.usr).subscribe((x) => {this.usr = x;
    localStorage.setItem("username", this.usr.username);
    localStorage.setItem("email", this.usr.email);
    localStorage.setItem("userID", this.usr.id);
    localStorage.setItem("password", this.password);
    window.location.replace("http://localhost:4200/userProfile")
  },
    (err: HttpErrorResponse) => {
      if(err.error instanceof Error) {

      } else {
        this.username = "";
        this.password = "";
        this.email = "";
        document.getElementById("username").style.borderColor = "red";
        document.getElementById("password").style.borderColor = "red";
        document.getElementById("email").style.borderColor = "red";
      }
    });
  }

}
