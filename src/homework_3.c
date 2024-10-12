#include <stdio.h>
#include <stdint.h>
#include <math.h>
//#include "my_test.h"
//uint8_t
int write_message(FILE* stream, const void *buf, size_t nbyte){
	int count_of_bytes=0;
	for(int i=0;i<(int)nbyte;i++){
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

int read_message(FILE *stream, void *buf){
	uint8_t byte;
	uint8_t new_byte=0;
	uint8_t drawn_number=0;
	int count_of_one=0;
	int count_of_bytes=0;
	((uint8_t *)buf)[count_of_bytes]=0x7E;
	count_of_bytes++;
	int count_drawn_numbers=0;
	while(!feof(stream)){
		byte=getc(stream);
		if(byte==0xff){
			continue;
		}
		new_byte=0;
		int i=0;
		if (count_drawn_numbers==8){
			((uint8_t *)buf)[count_of_bytes]=drawn_number;
			count_of_bytes++;
			drawn_number=0;
			count_drawn_numbers=0;
		}
		else{
			if (count_drawn_numbers!=0){
				new_byte=new_byte | drawn_number;
				drawn_number=0;
				drawn_number= drawn_number | ((((int)(pow(2,count_drawn_numbers)-1)) & byte)<<(8-count_drawn_numbers));
				byte=byte>>count_drawn_numbers;
				i=count_drawn_numbers;
			}
		}
		for(int j=i;j<8;j++){
			if (count_of_one==5){
				drawn_number=drawn_number | (((int)(pow(2,count_drawn_numbers)) & byte)<<(7-count_drawn_numbers));
				count_drawn_numbers++;
				byte=byte>>1;
				count_of_one=0;
				continue;
			}
			if (byte & ((int)pow(2,(7-j)))){
				count_of_one++;
			}
			else{
				count_of_one=0;
			}
			new_byte=new_byte | (byte & ((int)pow(2,(7-j))));
		}
		((uint8_t *)buf)[count_of_bytes]=new_byte;
		count_of_bytes++;
	}
	if (count_drawn_numbers!=0){
		((uint8_t *)buf)[count_of_bytes]= drawn_number | (0x7E>>count_drawn_numbers);
		count_of_bytes++;
		((uint8_t *)buf)[count_of_bytes]=(0x7E<<(8-count_drawn_numbers)) | (((int)(pow(2,8-count_drawn_numbers)))-1);
		count_of_bytes++;
	}
	else{
		((uint8_t *)buf)[count_of_bytes]=0x7E;
		count_of_bytes++;
	}
	return count_of_bytes;
}