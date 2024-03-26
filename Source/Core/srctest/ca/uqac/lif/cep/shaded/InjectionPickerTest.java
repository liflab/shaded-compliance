package ca.uqac.lif.cep.shaded;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

public class InjectionPickerTest
{
	@Test
	public void test1()
	{
		InjectionPicker picker = new InjectionPicker(2, 3);
		int num = 0;
		while (!picker.isDone())
		{
			Integer[] injection = picker.pick();
			num++;
			System.out.println(Arrays.toString(injection));
		}
		assertEquals(num, numInjections(2, 3));
	}
	
	@Test
	public void test2()
	{
		InjectionPicker picker = new InjectionPicker(2, 4);
		int num = 0;
		while (!picker.isDone())
		{
			Integer[] injection = picker.pick();
			num++;
			System.out.println(Arrays.toString(injection));
		}
		assertEquals(num, numInjections(2, 4));
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
