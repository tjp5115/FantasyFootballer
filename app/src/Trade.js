import React, { Component } from 'react';
import './Trade.css';
import Player from './Player';

class Trade extends Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      leagueId: props.leagueId,
      teamId: props.teamId,
      leagueName: props.leagueName,
      leagueType: props.leagueType,
      isLoaded: null,
      items: [],
    };
  }

  componentDidMount(){
    updateWithPlayers(this);
  }

  componentDidUpdate() {
    const { leagueId, teamId, isLoaded } = this.state;

    if ( this.props.leagueId === leagueId ||  this.props.teamId === teamId ){
      return;
    }

    if( isLoaded ){
      this.setState({
        isLoaded: false
      })
    }

    updateWithPlayers(this);
  }


  render() {
    const { error, isLoaded, items, leagueName } = this.state;
    
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <div><h1> Possible Trades </h1><p>Loading...</p></div>;
    } else {
      return <div>
        <h1> Possible Trades </h1>
        <h2>{leagueName}</h2> 
        {items.map(playerTrade => 
          <div>
            <h3 className="position-header">{playerTrade.position}</h3>
            <div className="player-container">
              <div className="player-fixed">
                <p className="player-trade-header">Possible drops</p>
                <table>
                  <tbody>
                    {getPlayerHeader()}
                    {getPlayers(playerTrade.possiblePlayersToDrop)}
                    
                  </tbody>
                </table> 
              </div>
              <div className="player-item">
                <p className="player-trade-header"> Possible Pickups </p>
                <table>
                  <tbody>
                    {getPlayerHeader()}
                    {getPlayers(playerTrade.possiblePlayersToPickUp)}
                  </tbody>
                </table> 
              </div>
            </div>
          </div>
        )}
      </div> 
    }
  }
}

function updateWithPlayers(trade){
  const {leagueId, teamId, leagueType} = trade.props;
  fetch("http://localhost:8080/footballer/espn/trade?leagueID="+leagueId+"&teamID="+teamId+"&leagueType="+leagueType)
    .then(res => res.json())
    .then(
      (result) => {
        trade.setState({
          isLoaded: true,
          items: result.data.items,
          leagueId: leagueId,
          teamId: teamId
        });
      },
      // Note: it's important to handle errors here
      // instead of a catch() block so that we don't swallow
      // exceptions from actual bugs in components.
      (error) => {
        trade.setState({
          isLoaded: true,
          error
        });
      }
    )
  }


function getPlayers(players){
  return players.map(player => <Player player={player}/>)
}

function getPlayerHeader(){
  return <Player header={true}/>
}



export default Trade;
