/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
'use strict';

import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    ListView,
    Button,
    Image,
    Navigator, TouchableHighlight
} from 'react-native';


export default class EventDetailPage extends Component {


    constructor(props) {
        super(props);

    }

    componentDidMount() {
    }

    componentWillUnmount() {

    }


    render() {

        return (
            <TouchableHighlight onPress={() => {

            this.props.navigator.pop();

        }}>
                <Text>Hello {this.props.title}!</Text>
            </TouchableHighlight>
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

