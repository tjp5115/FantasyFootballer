import React, { Component } from 'react';
import './App.css';
import Trade from './Trade';

class App extends Component {

  render() {
            //makeMyESPNTeamBetter("BLAZ", 102116, 4);
        //makeMyESPNTeamBetter("BURN", 155338, 15);
        //makeMyESPNTeamBetter("DAD", 1002004, 11);
    return <div>
      <Trade leagueId="102116" teamId="4" leagueName="BLAZ"/>        
      <Trade leagueId="155338" teamId="15" leagueName="BURN"/>        
      <Trade leagueId="1002004" teamId="11" leagueName="DAD"/>        
     </div>
  }

}

export default App;
