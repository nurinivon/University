myset: myset.o set.o ./utils/utils.o
	gcc -ansi -Wall -pedantic myset.o set.o ./utils/utils.o -o myset
./utils/utils.o: ./utils/utils.c
	gcc -ansi -Wall -pedantic ./utils/utils.c -c -o ./utils/utils.o
set.o: set.c
	gcc -ansi -Wall -pedantic set.c -c
myset.o: myset.c
	gcc -ansi -Wall -pedantic myset.c -c
