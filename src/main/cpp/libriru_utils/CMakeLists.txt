cmake_minimum_required(VERSION 3.0)
project(riru_utils)

add_subdirectory(libxhook)
add_subdirectory(libriru)

set(CMAKE_CXX_STANDARD 14)

add_library(riru_utils STATIC riru_utils.h riru_utils.c)
target_link_libraries(riru_utils riru xhook)
target_include_directories(riru_utils PRIVATE libxhook libriru)

