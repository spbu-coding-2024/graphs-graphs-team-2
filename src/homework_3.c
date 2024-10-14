#include <stdio.h>
#include <stdint.h>
//#include <math.h>
//#include "my_test.h"
float pow_my(int number, int degree){ // функция возведения числа в степень
	float return_number = 1.0;
	for(int i = 0; i < degree; i++){
		return_number = return_number*number;
	}
	return return_number;
}

int write_message(FILE* stream, const void *buf, size_t nbyte){
	uint8_t byte = 0;
	uint8_t new_byte = 0;
	uint8_t drawn_number = 0; // бит(ы) которые будут съезжать после вставки 0
	int count_drawn_numbers = 0;
	int count_of_one = 0;
	putc(0x7E, stream);
	if (ferror(stream)){
		fprintf(stderr, "Error writing to file!\n");
		return EOF;
	}
	for(int k = 0; k < (int)nbyte; k++){
		byte = ((uint8_t *)buf)[k];
		new_byte = 0;
		if (count_drawn_numbers == 8){ // если сьехало 8 битов то нужно записать их в отдельный байт 
			putc(drawn_number, stream);
			if (ferror(stream)){
				fprintf(stderr, "Error writing to file!\n");
				return EOF;
			}
			drawn_number = 0; // обновляем счетчики(теперь нет съехавших битов)
			count_drawn_numbers = 0;
		}
		else{
			if (count_drawn_numbers != 0){ // если есть съехавшие биты то их нужно записать в следующий байт
				new_byte = new_byte | drawn_number;
				drawn_number = 0; //для записи новых съехавших битов
				int mask_of_bits_moved = (int)(pow_my(2, count_drawn_numbers) - 1); // получаем 1 на местах съехавших битов
				int numbers_of_bits_moved = mask_of_bits_moved & byte; // получаем эти биты
				numbers_of_bits_moved = numbers_of_bits_moved << (8 - count_drawn_numbers); // двигаем их вперед
				drawn_number = drawn_number | numbers_of_bits_moved; // записываем их
				byte = byte >> count_drawn_numbers; //убираем съехавшие биты из byte
			}
		}
		byte = byte | new_byte; //записываем биты которые сьехали с предыдущего байта
		for(int j = 0; j < 8; j++){
			if (count_of_one == 5){
				drawn_number = drawn_number | ((byte & 1) << (7 - count_drawn_numbers)); // если един стало 5 то последний бит выпадает(двигаем вначало)
				count_drawn_numbers++;
				byte = byte >> 1; // двигаем byte на выпавший бит
				count_of_one = 0;
				continue; //контин чтобы вписать 0
			}
			int parsed_bit = (int)pow_my(2, (7 - j));
			if (byte & parsed_bit){ // проверка на 1 в нужном нам бите
				count_of_one++;
			}
			else{
				count_of_one=0;
			}
			new_byte = new_byte | (byte & parsed_bit); // запись бита в new_byte
		}
		putc(new_byte, stream);
	}
	if (count_drawn_numbers != 0){ //если после считывания всех байтов есть сьехавшие биты то нужно добавить маркер окончания и заполнить конец след байта 1
		int end_marker_in_1_byte = 0x7E >> count_drawn_numbers;
		int end_marker_in_2_byte = 0x7E << (8 - count_drawn_numbers);
		int ones_padding_in_byte_2 = (int)(pow_my(2, 8 - count_drawn_numbers)) - 1;
		putc(drawn_number | end_marker_in_1_byte, stream); // добавляем к выпавшим битам помещяющийся в этот байт маркер конца
		putc(end_marker_in_2_byte | ones_padding_in_byte_2, stream);
	}
	else{
		putc(0x7E, stream); // нет съехавших битов просто вставляем маркер конца
	}
	return (int)(nbyte);
}

int read_message(FILE *stream, void *buf){
	int count_of_one = 0;
	int count_of_bits = 0;
	int flag_of_start = 0;
	int new_byte = 0;
	int count_of_bytes = 0;
	uint8_t byte = 0;
	while(!feof(stream)){
		byte = getc(stream);
		if (ferror(stream)){
			fprintf(stderr, "File reading error!!!\n");
			return EOF;
		}
		for(int i = 0; i < 8; i++){
			if (flag_of_start == 1){ // если до этого был маркер начала
				if (count_of_one != 5){ // если это не 0 идущий после 5 единиц
					count_of_bits++;
					int parsed_bit = (int)(pow_my(2, 7 - i));
					int move_to_first_bit = ((parsed_bit & byte) << i);
					new_byte = new_byte | (move_to_first_bit >> (count_of_bits - 1)); // сдвиг в позицию бита по счету и запись в new_byte
					if (count_of_bits == 8){ // если заполнился байт
						((uint8_t *)buf)[count_of_bytes] = new_byte; // запись байта
						count_of_bytes++;
						new_byte = 0;
						count_of_bits = 0;
					}
				}
			}
			int parsed_bit = (int)pow_my(2, (7 - i));
			if (byte & parsed_bit){
				count_of_one++;
			}
			else{
				if (count_of_one == 6){ // если встретили маркер начала или конца
					if (flag_of_start == 0){ // если маркер начала
						flag_of_start = 1;
					}
					else{
						if(count_of_bits != 7){ // если нецелое число байтов передалось
							fprintf(stderr, "Payload contains a non-integer number of bytes!!!\n");
							return EOF;
						}
						return count_of_bytes;
					}
				}
				count_of_one = 0;
			}
		}
	}
	fprintf(stderr, "The message contains an incorrect bit sequence!!!\n"); // если не встретился маркер
	return EOF;
}
