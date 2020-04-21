package me.RandomPredictor;

import java.util.List;

public class MultipleSeedsException extends Exception {

	private final List<Long> seeds;

	public MultipleSeedsException(List<Long> seeds) {
		this.seeds = seeds;
	}

	/**
	 * 
	 * @return a {@link java.util.List List} containing all possible seeds, which
	 *         can be passed into
	 *         <code>new {@link java.util.Random#Random(long) Random(long seed)}</code>
	 */
	public List<Long> getSeeds() {
		return seeds;
	}

	@Override
	public String getMessage() {
		return "Number sequence has " + seeds.size() + " possible seeds";
	}

}
