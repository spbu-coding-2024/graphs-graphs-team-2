#include <stdio.h>
#include <stdint.h>
#include "my_test.h"
//uint8_t
int write_message(FILE* stream, const void *buf, size_t nbyte){
	int count_of_bytes=0;
	for(int i=0;i<nbyte;i++){
		uint8_t byte;
		byte=((uint8_t *)buf)[i];
		if (putc(byte,stream)==EOF){
			fprintf(stderr,"Write error!");
			return EOF;
		}
		count_of_bytes++;
	}
	return count_of_bytes;
}