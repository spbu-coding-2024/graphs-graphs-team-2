#include <stdio.h>
#include <stdint.h>
#include <math.h>
#include "my_test.h"

int write_message(FILE* stream, const void *buf, size_t nbyte){
	uint8_t byte;
	uint8_t new_byte=0;
	uint8_t drawn_number=0;
	int count_of_one=0;
	int count_of_bytes=0;
	putc(0x7E,stream);
	if (ferror(stream)){
		fprintf(stderr,"Error writing to file!\n");
		return EOF;
	}
	count_of_bytes++;
	int count_drawn_numbers=0;
	for (int k=0;k<(int)nbyte;k++){
		byte=((uint8_t *)buf)[k];
		new_byte=0;
		int i=0;
		if (count_drawn_numbers==8){
			putc(drawn_number,stream);
			if (ferror(stream)){
				fprintf(stderr,"Error writing to file!\n");
				return EOF;
			}
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
		putc(new_byte,stream);
		count_of_bytes++;
	}
	if (count_drawn_numbers!=0){
		putc(drawn_number | (0x7E>>count_drawn_numbers),stream);
		count_of_bytes++;
		putc((0x7E<<(8-count_drawn_numbers)) | (((int)(pow(2,8-count_drawn_numbers)))-1),stream);
		count_of_bytes++;
	}
	else{
		putc(0x7E,stream);
		count_of_bytes++;
	}
	return count_of_bytes;
}

int read_message(FILE *stream, void *buf){
	int count_of_one=0;
	int count_of_bits=0;
	int flag_of_start=0;
	int new_byte=0;
	int count_of_bytes=0;
	uint8_t byte;
	while(!feof(stream)){
		byte=getc(stream);
		if (ferror(stream)){
			fprintf(stderr,"File reading error!!!\n");
			return EOF;
		}
		for(int i=0;i<8;i++){
			if (flag_of_start==1){
				if (count_of_one!=5){
					count_of_bits++;
					new_byte=new_byte | (((((int)(pow(2,7-i))) & byte) << i) >> (count_of_bits-1));
					if (count_of_bits==8){
						((uint8_t *)buf)[count_of_bytes]=new_byte;
						count_of_bytes++;
						new_byte=0;
						count_of_bits=0;	
					}
				}
			}
			if (byte & ((int)pow(2,(7-i)))){
				count_of_one++;
			}
			else{
				if (count_of_one==6){
					if (flag_of_start==0){
						flag_of_start=1;
					}
					else{
						if(count_of_bits!=7){
							fprintf(stderr,"Payload contains a non-integer number of bytes!!!\n");
							return EOF;
						}
						return count_of_bytes;
					}
				}
				count_of_one=0;
			}
		}
	}
	fprintf(stderr,"The message contains an incorrect bit sequence!!!\n");
	return EOF;
}

