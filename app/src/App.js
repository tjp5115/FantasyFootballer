import React, { Component } from 'react';
import './App.css';
import League from './League';
import Fanduel from './Fanduel';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

class App extends Component {

  render(){
    return router()
  }

}

  function getTeams(){
    return {
      "data" : {
        "items" : [ 
          {"leagueId":1002004,"teamId":18,"name":"Trip", "leagueType":"NORMAL"},
          {"leagueId":155338,"teamId":15,"name":"BURN",  "leagueType":"PPR"},
        ]
      }
    }
  }

  function router(){
    return <Router>
      <div>
        <ul>
          <li>
            <Link to="/fanduel">Fanduel</Link>
          </li>
        {getTeams().data.items.map(league => 
          <li>
          
            <Link to={{ pathname: "/league", search:"?leagueId="+league.leagueId+"&teamId="+league.teamId+"&leagueType="+league.leagueType}} >
              {league.name}
            </Link>
          </li>
        )}
        </ul>
        <Route path="/league" component={League}/>
        <Route path="/fanduel" component={Fanduel}/>
      </div>
    </Router>
  }
export default App;
