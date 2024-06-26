#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RnMetronome, NSObject)

RCT_EXTERN_METHOD(play: (nonnull NSNumber *) beatsPerMinute)
RCT_EXTERN_METHOD(stop)

// RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
//                  withResolver:(RCTPromiseResolveBlock)resolve
//                  withRejecter:(RCTPromiseRejectBlock)reject)

// + (BOOL)requiresMainQueueSetup
// {
//   return NO;
// }

@end
