import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post.service';
import { User } from '../../models/user';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username : string;
  password : string;
  myUserTest: string;
  //public usr: User;

  constructor(private postService: PostService, private sessionService: SessionService) { }

  ngOnInit() {
    this.sessionCheck()
  }


  usr: User = {
    id: undefined,
    email: undefined,
    role: undefined,
    username: undefined,
    password: undefined
  }
  login() {
    this.usr.username = this.username;
    this.usr.password = this.password;

    
    this.postService.login(this.usr).subscribe((x) => { this.usr = x;
      localStorage.setItem("email", this.usr.email); 
      localStorage.setItem("userID", this.usr.id);
      localStorage.setItem("role", this.usr.role); 
      localStorage.setItem("username", this.usr.username);
      localStorage.setItem("password", this.password);
      window.location.replace("http://localhost:4200/userProfile")
    },
      (err: HttpErrorResponse) => {
        if(err.error instanceof Error) {

        } else {
          this.username =  "";
          document.getElementById("username").style.borderColor = "red";
          this.password = "";
          document.getElementById("password").style.borderColor = "red";
          alert("The entered credentials are invalid! Please try again");
        }
    })
  }

  session : boolean
  sessionCheck() {
    this.session = this.sessionService.sessionCheck();
  }

  logout() {
    localStorage.clear();
    window.location.replace("http://localhost:4200/home");
  }

}
