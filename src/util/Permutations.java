package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Permutations {
	
	public List permutations = new ArrayList<>();
	
	public Permutations(getA, getB, int positionCount){
//        var permutations = createPermutations(getA, getB, positionCount);
//
//        this.getRandomPermutationPair = function () {
//            var i = Math.floor( Math.random() * permutations.length );
//            return [
//                permutations[permutations.length - 1 - i],
//                permutations[i]
//            ];
//        };
	}
	
	public getRandomPermutation() {
		Random r = new Random();
		return permutations.get(r.nextInt(permutations.size())); 
	}
	
	public String createBinaryNumberWithLeadingZeroes(Integer positionCount, Integer number){
		
		String binaryString  = Integer.toString(number, 2);
//        double leadingZeroCount = positionCount - binaryString.length();
//        if (leadingZeroCount != 0) {
//            binaryString = new Array(leadingZeroCount+1).join("0") + binaryString;
//        }
        return String.format("%0" + positionCount + "d", binaryString); // TODO works?
//        return binaryString;
	}
	
//	public createPermutations (a, b, positionCount){
//		var values = [a,b];
//        var result = [];
//
//        var permutationCount = Math.pow(2, positionCount);
//        while (permutationCount--) {
//            var permutationString  = createBinaryNumberWithLeadingZeroes(positionCount, permutationCount);
//
//            var permutationArray = permutationString.split('');
//            var permutationValues = [];
//            for (var i=0; i<permutationArray.length; i++) {
//                permutationValues.push(values[permutationArray[i]]);
//            }
//            result.push(permutationValues);
//        }
//
//        return result;
//	}
//	
}

