import React, { Component } from 'react';
import './Fanduel.css';
import axios from 'axios';

class Fanduel extends Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      loaded: 0,
      selectedFile: null,
      items: new Map(),
    };
  }

  handleselectedFile = event => {
    this.setState({
      selectedFile: event.target.files[0],
      loaded: 0,
    })
  }

  handleUpload = () => {
    const data = new FormData();
    data.append('file', this.state.selectedFile);
    const config = {
        headers: {
            'content-type': 'multipart/form-data'
        }
    }
    axios.post("http://localhost:8080/footballer/fanduel/lineup", data, {
        dataonUploadProgress: {
          onUploadProgress: ProgressEvent => {
            this.setState({
              loaded: (ProgressEvent.loaded / ProgressEvent.total*100),
            })
          }
        }
      }
    )
    .then(
      (result) => {
        this.setState({
          isLoaded: true,
          items: this.groupByTierAndPosition(result.data.data.items),
        });
      },
      (retError) => {
        this.setState({
          isLoaded: true,
          error : retError.stack
        });
      }
    )
  }

  groupByTierAndPosition(players){
    const map = {}

    players.forEach((player) => {
      const tier = player.tier;
      const position = player.position;
      if(!map[tier]){
        map[tier] = {};
      }

      if(!map[tier][position]){
        map[tier][position] = [];
      }

      map[tier][position].push(player);
    });
    return map;
  }

  renderPlayerTable() {
    const { items } = this.state;
    if(!items){
      return ""
    }
  }

  render(){
    const { items,error } = this.state;

    return <div>
      <h1>Fanduel</h1>
      <input type="file" onChange={this.handleselectedFile}/>
      <button onClick={this.handleUpload}>Upload</button>
      <p>{error ? error : null}</p>
      <div>  
        {Object.keys(items).map( tier =>
          <div>
            <h3>tier {tier}</h3>
            <div className='fanduel-player-container'>
              {Object.keys(items[tier]).map( (position, i) =>
                <div className={'fanduel-players-' + (i ? 'flex' : 'fixed')}>
                  <h4>{position}</h4>
                  <table>
                    <tr><th>Name</th><th>Salary</th></tr>
                    {items[tier][position].map(player =>
                          <tr> 
                            <td>{player.firstName} {player.lastName}</td>
                            <td>{player.salary}</td>
                          </tr>
                    )}
                  </table>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  }

}



function getPlayers(){
  return {
    "data" : {
      "items" : [ 
        {firstName:"Tyler", lastName:"Paulsen",position:"QB",tier:1,salary:1000},
        {firstName:"Crystal", lastName:"Errington",position:"QB",tier:1,salary:3200},
        {firstName:"Shawn", lastName:"Ferris",position:"RB",tier:3,salary:2000},
        {firstName:"Dolphins", lastName:"",position:"DST",tier:3,salary:5000},
      ]
    }
  }
}
export default Fanduel;