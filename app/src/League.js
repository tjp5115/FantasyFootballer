import React, { Component } from 'react';
import './League.css';
import Trade from './Trade';
import Lineup from './Lineup';
import queryString from 'query-string'

class League extends Component {

  render(){
    if( !this.props.location ){
      return ""
    } else {
    const values = queryString.parse(this.props.location.search);
    return <div className="league-container">
      <div className="trade">
        <Trade teamId={values.teamId} leagueId={values.leagueId} />
      </div>
      <div className="lineup">
        <Lineup  teamId={values.teamId} leagueId={values.leagueId} />
      </div>
    </div>
    }
  }

}
export default League;
