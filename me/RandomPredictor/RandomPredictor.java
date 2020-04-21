package me.RandomPredictor;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class gives you the ability to easily bruteforce the current seed of an
 * instance of the {@link java.util.Random Random} class just by knowing some
 * values generated by it.<br>
 * <br>
 * An example of this would be:<br>
 * <br>
 * <code>
 * double d = {@link java.lang.Math#random() Math.random()};<br>
 * {@link java.util.Random Random} rand = {@link me.RandomPredictor.RandomPredictor#getRandom(double) RandomPredictor.getRandom(d)};
 * </code><br>
 * <br>
 * The expression <code>Math.random() == rand.nextDouble()</code> will be
 * true.<br>
 * You successfully predicted the output of your next call to
 * {@link java.lang.Math#random() Math.random()}!
 *
 */
public class RandomPredictor {

	private static final long multiplier = 25214903917L;
	private static final long addend = 11L;
	private static final long mask = 281474976710655L;

	/**
	 * This method will, given two float values generated by
	 * {@link java.util.Random#nextFloat() Random.nextFloat()}, return another
	 * {@link java.util.Random Random} instance, which will generate the same
	 * numbers as the instance from which the values were generated in the first
	 * place.<br>
	 * <br>
	 * 
	 * This method is highly unlikely to actually return one {@link java.util.Random
	 * Random} instance, because there are usually two or three possible seeds due
	 * to the small bit count of floats. If you have three values, see
	 * {@link RandomPredictor#getRandom(float, float, float) this} method instead.
	 * 
	 * @param n1 the first <code>float</code> value generated by
	 *           {@link java.util.Random#nextFloat() Random.nextFloat()}
	 * @param n2 the second <code>float</code> value generated by
	 *           {@link java.util.Random#nextFloat() Random.nextFloat()}
	 * @return an instance of {@link java.util.Random Random} containing the found
	 *         seed
	 * @throws NoSeedException        if no seed could be found for these values
	 * @throws MultipleSeedsException if there are more than one possible seeds, the
	 *                                exception conatins a {@link java.util.List
	 *                                List} with all possible seeds
	 */
	public static Random getRandom(float n1, float n2) throws NoSeedException, MultipleSeedsException {
		long num = (long) (n1 * (1L << 24));
		long num2 = (long) (n2 * (1L << 24));
		return getRandomInternal(num, num2, 24, 24);
	}

	/**
	 * This method will, given three float values generated by
	 * {@link java.util.Random#nextFloat() Random.nextFloat()}, return another
	 * {@link java.util.Random Random} instance, which will generate the same
	 * numbers as the instance from which the values were generated in the first
	 * place.<br>
	 * <br>
	 * 
	 * This method is more likely to return a valid {@link java.util.Random Random}
	 * instance, since there are three values to check against.
	 * 
	 * @param n1 the first <code>float</code> value generated by
	 *           {@link java.util.Random#nextFloat() Random.nextFloat()}
	 * @param n2 the second <code>float</code> value generated by
	 *           {@link java.util.Random#nextFloat() Random.nextFloat()}
	 * @param n3 the third <code>float</code> value generated by
	 *           {@link java.util.Random#nextFloat() Random.nextFloat()}
	 * @return an instance of {@link java.util.Random Random} containing the found
	 *         seed
	 * @throws NoSeedException        if no seed could be found for these values
	 * @throws MultipleSeedsException if there are more than one possible seeds, the
	 *                                exception conatins a {@link java.util.List
	 *                                List} with all possible seeds
	 */
	public static Random getRandom(float n1, float n2, float n3) throws NoSeedException, MultipleSeedsException {
		long num = (long) (n1 * (1L << 24));
		long num2 = (long) (n2 * (1L << 24));
		long num3 = (long) (n3 * (1L << 24));

		try {
			Random r = getRandomInternal(num, num2, 24, 24);
			// this is the seed which will give the third value,
			// so we need to run nextFloat once
			r.nextFloat();
			return r;
		} catch (MultipleSeedsException e) {
			for (Long seed : e.getSeeds()) {
				long l = (((seed.longValue() ^ multiplier) & mask) * multiplier + addend) & mask;
				if ((int) (l >>> 24) == num3) {
					return new Random(l ^ multiplier);
				}
			}
			throw new NoSeedException();
		}
	}

	/**
	 * This method will, given one double value generated by
	 * {@link java.util.Random#nextDouble() Random.nextDouble()}, return another
	 * {@link java.util.Random Random} instance, which will generate the same
	 * numbers as the instance from which the values were generated in the first
	 * place.<br>
	 * <br>
	 * 
	 * This method also works for {@link java.lang.Math#random() Math.random()}. The
	 * following example should always return true:<br>
	 * <br>
	 * 
	 * <code>RandomPredictor.getRandom(Math.random()).nextDouble() == Math.random()</code>
	 * 
	 * @param n a <code>double</code> value generated by
	 *          {@link java.util.Random#nextDouble() Random.nextDouble()}
	 * @return an instance of {@link java.util.Random Random} containing the found
	 *         seed
	 * @throws NoSeedException        if no seed could be found for this value
	 * @throws MultipleSeedsException if there are more than one possible seeds, the
	 *                                exception conatins a {@link java.util.List
	 *                                List} with all possible seeds
	 */
	public static Random getRandom(double n) throws NoSeedException, MultipleSeedsException {
		long num = (long) (n * (1L << 53));
		return getRandomInternal((int) (num >>> 27), (int) (num & ((1L << 27) - 1)), 22, 21);
	}

	/**
	 * This method will, given one long value generated by
	 * {@link java.util.Random#nextLong() Random.nextLong()}, return another
	 * {@link java.util.Random Random} instance, which will generate the same
	 * numbers as the instance from which the values were generated in the first
	 * place.
	 * 
	 * @param n a <code>long</code> value generated by
	 *          {@link java.util.Random#nextLong() Random.nextLong()}
	 * @return an instance of {@link java.util.Random Random} containing the found
	 *         seed
	 * @throws NoSeedException        if no seed could be found for this value
	 * @throws MultipleSeedsException if there are more than one possible seeds, the
	 *                                exception conatins a {@link java.util.List
	 *                                List} with all possible seeds
	 */
	public static Random getRandom(long n) throws NoSeedException, MultipleSeedsException {
		long v1 = (int) (n >> 32);
		long v2 = (int) (n & ((1L << 32) - 1));
		if (v2 < 0)
			v1++;
		return getRandomInternal(v1, v2, 16, 16);
	}

	/**
	 * This method will, given two int values generated by
	 * {@link java.util.Random#nextInt() Random.nextInt()}, return another
	 * {@link java.util.Random Random} instance, which will generate the same
	 * numbers as the instance from which the values were generated in the first
	 * place.<br>
	 * <br>
	 * 
	 * This method does NOT work for values generated by
	 * {@link java.util.Random#nextInt(int) Random.nextInt(int n)}.
	 * 
	 * @param n1 the first <code>int</code> value generated by
	 *           {@link java.util.Random#nextInt() Random.nextInt()}
	 * @param n2 the second <code>int</code> value generated by
	 *           {@link java.util.Random#nextInt() Random.nextInt()}
	 * @return an instance of {@link java.util.Random Random} containing the found
	 *         seed
	 * @throws NoSeedException        if no seed could be found for these values
	 * @throws MultipleSeedsException if there are more than one possible seeds, the
	 *                                exception conatins a {@link java.util.List
	 *                                List} with all possible seeds
	 */
	public static Random getRandom(int n1, int n2) throws NoSeedException, MultipleSeedsException {
		return getRandomInternal((long) n1, (long) n2, 16, 16);
	}

	/**
	 * This method will, given a byte array generated by
	 * {@link java.util.Random#nextBytes(byte[]) Random.nextBytes(byte[] arr)},
	 * return another {@link java.util.Random Random} instance, which will generate
	 * the same numbers as the instance from which the values were generated in the
	 * first place.<br>
	 * <br>
	 * 
	 * This method only uses the first 8 bytes of the given array to determine the
	 * seed.
	 * 
	 * @param arr the <code>byte[]</code> generated by
	 *            {@link java.util.Random#nextBytes(byte[]) Random.nextBytes(byte[]
	 *            arr)}
	 * @return an instance of {@link java.util.Random Random} containing the found
	 *         seed
	 * @throws IllegalArgumentException if the given array is shorter than 8 bytes
	 * @throws NoSeedException          if no seed could be found for these values
	 * @throws MultipleSeedsException   if there are more than one possible seeds,
	 *                                  the exception conatins a
	 *                                  {@link java.util.List List} with all
	 *                                  possible seeds
	 */
	public static Random getRandom(byte[] arr) throws NoSeedException, MultipleSeedsException {
		if (arr.length < 8)
			throw new IllegalArgumentException("arr.length < 8");
		int n1 = 0;
		int n2 = 0;
		for (int i = 0; i < 4; i++) {
			n1 |= (arr[i] & 0xFF) << 8 * i;
		}
		for (int i = 4; i < 8; i++) {
			n2 |= (arr[i] & 0xFF) << 8 * (i - 4);
		}
		Random r = getRandomInternal((long) n1, (long) n2, 16, 16);
		int lenLeft = arr.length - 8;
		if (lenLeft > 0) {
			lenLeft = (lenLeft + 3) / 4;
			for (; lenLeft > 0; lenLeft--) {
				r.nextInt();
			}
		}
		return r;
	}

	private static Random getRandomInternal(long v1, long v2, int bits1, int bits2)
			throws NoSeedException, MultipleSeedsException {
		ArrayList<Long> seeds = new ArrayList<>(1);
		for (long l = v1 << bits1; l < (v1 + 1) << bits1; l++) {
			if ((int) (((l * multiplier + addend) & mask) >>> bits2) == v2) {
				seeds.add(Long.valueOf(((l * multiplier + addend) & mask) ^ multiplier));
			}
		}
		int size = seeds.size();
		if (size == 0)
			throw new NoSeedException();
		if (size == 1)
			return new Random(seeds.get(0).longValue());
		throw new MultipleSeedsException(seeds);
	}

}
