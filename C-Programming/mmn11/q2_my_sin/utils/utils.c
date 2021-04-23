#define PRECISION_LEVEL 0.00001

/*
the following method is a recursive method get a number as param and returns its factorial (x!)
*/
double factorial(int x)
{
	if(x <= 1) /*stop condition for the recursive method*/
	{
		return 1;
	}
	else
	{
		return x * factorial(x - 1); /*recursive call to the method*/
	}
} /*end of method factorial*/

/*
the following method is replacing the standard power method. it gets the base and the exponent of the power and returns the result
*/
double power(double base,int exponent)
{
	double result = 1; /*initalize the result*/
	int i; /*loop index*/
	for (i = 1; i <= exponent; i++)
	{
		result *= base; /*power implementation*/
	}
	return result;
} /*end of method power*/

/*
the following method is replacing the standard sin method.
this method is using the taylor series for sin with precision of 0.000001.
this method get a number (double) as a parameter and returns its sin.
*/
double my_sin(double x)
{
	int multiplier = -1; /*the multiplier of the next node that will merge with the current calculated result, exch iteration changes its sign*/
	double nextNode = 0; /*each iteration there will be a node that will merge with the current calculated result according to sin taylor series*/
	double result = 0; /*the result retuns from the method*/
	int i = 0; /*loop index*/
	int exponent; /*the exponent used to calculate the next node, growing each iteration*/
	if(x < 0) /*if x is negative, we will switch sign for it and for the multipiler*/
	{
		multiplier = 1;
		x = x * -1;
	}
	do
	{
		multiplier = multiplier * -1; /*change multiplier sign*/
		exponent = (2 * i) + 1; /*exponent growth*/
		nextNode = (power(x,exponent) / factorial(exponent)); /*nextNode calculation*/
		result = result + (multiplier * nextNode); /*merge with result*/
		i++;
	} while(nextNode >= PRECISION_LEVEL); /*stop condition for the loop*/
	return result;
} /*end of method my_sin*/

