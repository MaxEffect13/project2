import { Component, OnInit } from '@angular/core';
import { Team } from '../../models/team';
import { PostService } from '../../services/post.service';
import { SessionService } from '../../services/session.service';
import { User } from '../../models/user';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  newTeam: string;
  public team: Team;
  public usr: User;
  username: string;
  password: string;
  email: string;
  currentPassword: string;
  constructor(private postService: PostService, private sessionService: SessionService) { }

  ngOnInit() {
    this.team = new Team();
    this.sessionCheck();
    this.legendCheck();
    this.username = localStorage.getItem("username");
    this.password = localStorage.getItem("password");
    this.email = localStorage.getItem("email");
    this.getTeams();
  }

  createTeam() {
    console.log(this.newTeam);
    this.postService.createTeam(this.newTeam).subscribe();
    window.location.reload();
  }

  teams: any[];
  getTeams() {
    let posts;
    this.postService.getUserTeams(localStorage.getItem("userID")).then((allPosts) => {posts = allPosts; this.teams = posts; console.log(this.teams)});
    console.log(this.teams)
  }

  session : boolean
  sessionCheck() {
    this.session = this.sessionService.sessionCheck();
  }

  isLegend : boolean
  legendCheck() {
    this.isLegend = this.sessionService.legendCheck();
  }

  logout() {
    localStorage.clear();
    window.location.replace("http://localhost:4200/home");
  }

  battleSearch(id: number) {
    console.log(id);
    localStorage.setItem("team1", id.toString());
    window.location.replace("http://localhost:4200/teamSearch");
  }

  edit() {
    document.getElementById("email").disabled = false;
    document.getElementById("password").disabled = false;
    document.getElementById("no-no").style.visibility = "visible";
    document.getElementById("edit").style.display = "none";
    document.getElementById("save").style.display = "inline";
  }

  save() {
    let y;
    this.postService.edit(this.email, this.password).subscribe((x) => {y = x;
      localStorage.setItem("email", this.email);
      localStorage.setItem("password", this.password);
      window.location.reload();
    },
      (err: HttpErrorResponse) => {
        if(err.error instanceof Error) {

        } else {
          this.password = "";
          document.getElementById("password").style.borderColor="red";
          this.email = "";
          document.getElementById("email").style.borderColor = "red";
          alert("Something went wrong, please try again!");
        }
    })
     // if(this.currentPassword === localStorage.getItem("password")) {
    // } else {
    //   document.getElementById("")
    // }
  }

  legend() {
    this.postService.makeLegend().subscribe();
    localStorage.setItem("role", 'y');
    window.location.reload();
  }

  removeTeam(id: number) {
    this.postService.removeTeam(id).subscribe();
    window.location.reload();
  }
}
