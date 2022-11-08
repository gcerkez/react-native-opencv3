
// @author Adam G. Freeman, adamgf@gmail.com

import { NativeModules } from 'react-native';
const  { RNOpencv3 } = NativeModules;  // this is imported from java files?

export async function jsRunPhash(imagePath) {
 return await RNOpencv3.runPhash(imagePath)
}