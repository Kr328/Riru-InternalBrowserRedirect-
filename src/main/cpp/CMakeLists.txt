cmake_minimum_required(VERSION 3.0)
project(riru_template)

include_directories(libriru_utils)
include_directories(libriru_utils/libriru)
include_directories(libriru_utils/libxhook)

add_subdirectory(libriru_utils)

add_library(riru_template SHARED main.c)
target_link_libraries(riru_template riru_utils log)

