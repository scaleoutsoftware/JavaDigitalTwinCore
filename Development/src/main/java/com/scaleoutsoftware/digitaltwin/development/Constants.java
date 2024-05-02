/*
 Copyright (c) 2023 by ScaleOut Software, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.scaleoutsoftware.digitaltwin.development;

class Constants {
    public static final int MAX_TIMER_COUNT = 5;
    /**
     *
     * Returns a hash that is suitable for inserting an object into a hash-based collection.
     * <p>
     * -----------------------------------------------------------------------------
     * MurmurHash3 was written by Austin Appleby, and is placed in the public
     * domain. The author hereby disclaims copyright to this source code.
     *
     * Note - The x86 and x64 versions do _not_ produce the same results, as the
     * algorithms are optimized for their respective platforms. You can still
     * compile and run any of them on any platform, but your performance with the
     * non-native version will be less than optimal.
     * Original code from:
     * https://github.com/aappleby/smhasher/blob/master/src/MurmurHash3.cpp
     * -----------------------------------------------------------------------------
     *
     * This implementation is tweaked to initialize with a hard-coded seed and to return a long instead of a
     * 32-bit unsigned integer (since Java doesn't easily support unsigned integers).
     * @param  data  The byte array to be hashed.
     * @return       Hash code for the array, with values ranging from 0 to 4,294,967,295.
     */
    static long getHash(byte[] data) {
        if(data == null) {
            throw new IllegalArgumentException("Hash data was null.");
        }

        final int seed = 947203; // Scaleout's implementation-specific seed.
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;

        int len = data.length;
        int h1 = seed;
        int roundedEnd = len & 0xfffffffc;  // round down to 4 byte block

        for (int i = 0; i < roundedEnd; i += 4) {
            // little endian load order
            int k1 = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) | ((data[i + 2] & 0xff) << 16) | (data[i + 3] << 24);
            k1 *= c1;
            k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
            k1 *= c2;

            h1 ^= k1;
            h1 = (h1 << 13) | (h1 >>> 19);  // ROTL32(h1,13);
            h1 = h1 * 5 + 0xe6546b64;
        }

        // tail (leftover bytes that didn't fit into a 4-byte block)
        int k1 = 0;

        switch (len & 0x03) {
            case 3:
                k1 = (data[roundedEnd + 2] & 0xff) << 16;
                // fallthrough
            case 2:
                k1 |= (data[roundedEnd + 1] & 0xff) << 8;
                // fallthrough
            case 1:
                k1 |= (data[roundedEnd] & 0xff);
                k1 *= c1;
                k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
                k1 *= c2;
                h1 ^= k1;
        }

        // finalization
        h1 ^= len;

        // fmix(h1);
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        // Other languages want to represent the hash as an unsigned int, but java doesn't easily have
        // unsigned types. So we move the signed integer's bits into an "unsigned" long, which
        // is big enough to hold all the positive values of an unsigned int.
        return h1 & 0x00000000ffffffffL;
    }
}
