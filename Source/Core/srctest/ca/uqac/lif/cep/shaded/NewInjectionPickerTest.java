package ca.uqac.lif.cep.shaded;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

public class NewInjectionPickerTest
{
	@Test
	public void test1()
	{
		NewerInjectionPicker picker = new NewerInjectionPicker(2, 3);
		int num = 0;
		while (!picker.isDone())
		{
			Integer[] injection = picker.pick();
			num++;
			System.out.println(Arrays.toString(injection));
		}
		assertEquals(numInjections(2, 3), num);
	}
	
	@Test
	public void test2()
	{
		NewerInjectionPicker picker = new NewerInjectionPicker(2, 4);
		int num = 0;
		while (!picker.isDone())
		{
			Integer[] injection = picker.pick();
			num++;
			System.out.println(Arrays.toString(injection));
		}
		assertEquals(numInjections(2, 4), num);
	}
	
	@Test
	public void testSkip0()
	{
		/* By asking skip 1 after the first call to pick, we effectively eliminate
		 * the enumeration of [2, 0] and [3, 0].
		 */
		NewerInjectionPicker picker = new NewerInjectionPicker(2, 2);
		assertArrayEquals(new Object[] {0, 1}, picker.pick());
		picker.forbid(1, 1);
		assertArrayEquals(new Object[] {1, 0}, picker.pick());
	}
	
	/* The following tests check the behavior of increment(). They expect
	 * mappings to be enumerated in this order:
	 * [0,1], [0,2], [0,3], [1,0], [1,2], [1,3], [2,0], [2,1], [2,3], [3,0]
	 * [3,1], [3,2]. */
	
	@Test
	public void testSkip1()
	{
		/* By asking skip 0 after the first call to pick, we effectively eliminate
		 * the enumeration of [0,2] and [0,3].
		 */
		NewerInjectionPicker picker = new NewerInjectionPicker(2, 4);
		assertArrayEquals(new Object[] {0, 1}, picker.pick());
		picker.forbid(0, 0);
		assertArrayEquals(new Object[] {1, 0}, picker.pick());
	}
	
	protected static int numInjections(final int n, final int m)
	{
		return factorial(n).multiply(binomial(m, n)).intValue();
	}
	
	/**
	 * Computes the binomial coefficient. This implementation is based on the
	 * answer on <a href="https://stackoverflow.com/a/2929897">StackOverflow</a>. 
	 * 
	 * @param N
	 *          the number of elements
	 * @param K
	 *          the number of elements to choose
	 * @return C(N,K)
	 */
	protected static BigInteger binomial(final int N, final int K)
	{
    BigInteger ret = BigInteger.ONE;
    for (int k = 0; k < K; k++) {
        ret = ret.multiply(BigInteger.valueOf(N-k))
                 .divide(BigInteger.valueOf(k+1));
    }
    return ret;
	}
	
	/**
	 * Calculates the factorial of a number.
	 * @param N the number
	 * @return N!
	 */
	protected static BigInteger factorial(final int N)
	{
		BigInteger ret = BigInteger.ONE;
		for (int i = 1; i <= N; i++)
		{
			ret = ret.multiply(BigInteger.valueOf(i));
		}
		return ret;
	}
}
