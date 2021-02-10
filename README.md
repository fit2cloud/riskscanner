# RiskScanner 公有云安全合规平台

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-red)](https://img.shields.io/badge/License-Apache%202.0-red)
[![HitCount](http://hits.dwyl.com/fit2cloudrd/fit2clolud20-risk-service.svg)](http://hits.dwyl.com/fit2cloudrd/fit2clolud20-risk-service.svg)

> [English](README_EN.md) | 中文

RiskScanner 是开源企业级公有云安全合规平台，公有云安全合规服务通过灵活多变的合规规则，对常见公有云平台（如AWS，Azure，Aliyun，Huawei，Tencent，K8s和GCP等）提供公有云资源的安全扫描，帮助企业有效提升公有云使用的合规性。

- 用于管理公共云帐户和资源的规则引擎。它允许用户定义策略以启用管理良好的云基础架构，既安全又优化成本。它将组织具有的许多脚本整合为一个轻量级且灵活的工具，并具有统一的指标和报告。

- 通过确保对安全策略（例如加密和访问要求），标签策略以及通过未使用资源的垃圾收集和非工作时间资源管理的成本管理的实时合规性，Compliance Service可用于管理AWS，Azure，Aliyun，Huawei，Tencent，K8s和GCP等环境。

- 设置策略以简单的YAML配置文件编写，使用户能够指定关于资源类型（EC2，ELB，EBS，ASG，AMI，RDS，VPC，S3，ECS，OSS）的策略，并由过滤器和操作的词汇表构成。

- 它与每个供应商的云本机无服务器功能集成在一起，以通过内置配置实时实施策略。或者，它可以作为简单的cron作业在服务器上运行，以针对大型现有集群执行。

## 技术栈

- 后端: [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- 前端: [Vue.js](https://vuejs.org/)
- 中间件: [MySQL](https://www.mysql.com/), [Kafka](https://kafka.apache.org/)
- 基础设施: [Docker](https://www.docker.com/), [Kubernetes](https://kubernetes.io/)

## License & Copyright

Copyright (c) 2014-2020 飞致云 FIT2CLOUD, All rights reserved.

Licensed under The GNU General Public License version 2 (GPLv2)  (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

https://www.gnu.org/licenses/gpl-2.0.html

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
