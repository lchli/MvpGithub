/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  ListView,
  Button,
   Image
} from 'react-native';


export default class AweProject extends Component {



constructor(props) {
    super(props);
   const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});

      this.state = {
      dataSource: ds.cloneWithRows([]),
      dss:ds


      };



  }

  componentDidMount() {
        this.onSearchChange();
    }

  componentWillUnmount() {
       
    }

//get data source.
 getDataSource2(movies) {
        return this.state.dss.cloneWithRows(movies);
    }


onSearchChange=()=>{

  fetch('https://api.github.com/events')
      .then((response) => response.json())
      .then((responseJson) => {

  this.setState({

                 dataSource: this.getDataSource2(responseJson)
             });


        return responseJson;
      })
      .catch((error) => {
        console.error(error);
      });
  }


renderRow=(jobj)=>{

  return (
    <View>
     <Text>{jobj.created_at}</Text>
    <Text>{jobj.actor.login}</Text>
     <Text>{jobj.repo.name}</Text>
    <Image source={{uri: jobj.actor.avatar_url}} style={{width: 40, height: 40}}/>
    </View>
    )
    
}



  render() {
    let that=this;
    return (
      <View style={styles.container}>
       
        <ListView
                dataSource={this.state.dataSource}
                renderRow={(e)=>this.renderRow(e)}
              />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#abcdef',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('AweProject', () => AweProject);
