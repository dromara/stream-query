# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

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
