assembler: main.o ./structures/structures.o ./app_utils/app_utils.o ./files/files.o ./resolver/resolver.o
	gcc -ansi -Wall -pedantic main.o ./structures/structures.o ./app_utils/app_utils.o ./files/files.o ./resolver/resolver.o -o assembler
./app_utils/app_utils.o: ./app_utils/app_utils.c
	gcc -ansi -Wall -pedantic ./app_utils/app_utils.c -c -o ./app_utils/app_utils.o
./structures/structures.o: ./structures/structures.c
	gcc -ansi -Wall -pedantic ./structures/structures.c -c -o ./structures/structures.o
./files/files.o: ./files/files.c
	gcc -ansi -Wall -pedantic ./files/files.c -c -o ./files/files.o
./resolver/resolver.o: ./resolver/resolver.c
	gcc -ansi -Wall -pedantic ./resolver/resolver.c -c -o ./resolver/resolver.o
main.o: main.c
	gcc -ansi -Wall -pedantic main.c -c
