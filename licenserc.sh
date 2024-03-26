#!/bin/bash

# 创建licenses目录
mkdir -p licenses

# 假设license-eye输出已重定向到此文件
license_eye_output="license-eye-output.txt"

# 处理license-eye输出
while IFS='|' read -r dependency license version; do
  # 清理读取的值
  dependency=$(echo "$dependency" | awk '{print $1}' | tr '.' '-' | tr ':' '-')
  license=$(echo "$license" | xargs)

  # 为每个依赖创建文件，这里简化处理，直接使用许可证信息
  echo "License: $license" > "licenses/license-$dependency.txt"

done < "$license_eye_output"

echo "License files have been generated in the licenses directory."
