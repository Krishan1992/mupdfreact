/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';
import RNMupdfViewer from 'react-native-mupdf-viewer';
import PdfDialog from './PdfDialog';
const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});


export default class App extends Component {
  render() {
    return (
      <View style={styles.container}>
        
        <Text style={styles.instructions} onPress={() => this.viewPdf()}>
          Select Pdf
        </Text>
      </View>
    );
  }
  selectPdf() {
    PdfDialog.openPdfDialog((success)=>{
        console.warn('success',success);
        RNMupdfViewer.openPdf(success);
    },(error)=>{
      console.warn('error',error);
    });
  }
  viewPdf() {
    RNMupdfViewer.openPdf('sample.pdf');
  }
}



const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
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
