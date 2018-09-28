import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post.service';
import { Router } from '@angular/router';
import { SessionService } from '../../services/session.service';
@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private postService: PostService, private sessionService: SessionService, private router: Router) { }

  ngOnInit() {
    this.getHeroes();
    this.sessionCheck();
  }

  heroes: any[];
  getHeroes(){
    let posts;
    // this.postService.getPosts().then((allPosts) => {posts = allPosts; console.log(posts.results[0].id)});
    this.postService.getHeroes().then((allPosts) => {posts = allPosts; this.heroes = posts.heroes; console.log(this.heroes)});
    
  }

  select(id: string) {
    location.replace("http://localhost:4200/heroProfile/?id="+id);
  }

  session: boolean
  sessionCheck() {
    this.session = this.sessionService.sessionCheck();
  }

  logout() {
    localStorage.clear();
    window.location.reload;
  }
}
