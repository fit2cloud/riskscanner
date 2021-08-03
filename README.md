<p align="center"><a href="https://riskscanner.io"><img src="https://fit2cloud2-offline-installer.oss-cn-beijing.aliyuncs.com/riskscanner/img/logo-dark.png" alt="DataEase" width="300" /></a></p>
<h3 align="center">开源多云安全合规扫描平台 <a href="https://github.com/riskscanner/riskscanner/blob/master/README_EN.md">[英文版]</a></h3>

<p align="center">
  <a href="https://app.codacy.com/gh/riskscanner/riskscanner?utm_source=github.com&utm_medium=referral&utm_content=riskscanner/riskscanner&utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/956d688c965044d49ec807817efd3ca0" alt="A"></a>
  <a href="https://www.gnu.org/licenses/old-licenses/gpl-2.0"><img src="https://img.shields.io/github/license/riskscanner/riskscanner?color=%00468F&style=flat-square" alt="License: GPL v2"></a>
  <a href="https://github.com/riskscanner/riskscanner/releases/latest"><img src="https://img.shields.io/github/v/release/riskscanner/riskscanner" alt=""></a>
  <a href="https://github.com/riskscanner/riskscanner"><img src="https://img.shields.io/github/stars/riskscanner/riskscanner?color=%231890FF&style=flat-square" alt=""></a>
  <a href="https://github.com/riskscanner/riskscanner/releases"><img src="https://img.shields.io/github/downloads/riskscanner/riskscanner/total" alt=""></a>
</p>
<hr />

RiskScanner `/ˈrɪskˌskænə(r)/` 是开源的多云安全合规扫描平台，基于 Cloud Custodian 和 Nuclei 引擎，实现对主流公(私)有云资源的安全合规扫描和漏洞扫描。

**功能优势**

> - [x] 等保 2.0 预检：符合等保 2.0 规范，覆盖安全审计、访问控制、入侵防范、网络架构和管理中心等各项检查；
> - [x] CIS 合规检查：符合 CIS 规范，检查和实时监控在云上的资源是否符合 CIS 要求；
> - [x] 漏洞扫描：通过对网络的扫描，发现可利用漏洞的安全检测（渗透攻击）；
> - [x] 最佳实践建议：制定合规管控基线，为企业级用户提供最佳实践建议，持续提升合规水平；

RiskScanner 遵循 GPL v2 开源协议，使用 SpringBoot/Vue 进行开发，界面美观、用户体验好，支持的公有云包括阿里云、腾讯云、华为云、Amazon Web Services、Microsoft Azure、Google Cloud，支持的私有云包括 OpenStack、VMware vSphere 等，并且支持漏洞扫描。

**技术优势**

> - [x] 规则简单灵活：扫描规则采用简单的 YAML 格式，简单易懂、并允许用户自定义规则；
> - [x] 支持多公(私)有云：支持的公有云包括阿里云、腾讯云、华为云、Amazon Web Services、Microsoft Azure、Google Cloud，支持的私有云包括 OpenStack、VMware vSphere 等；
> - [x] 支持多资源：支持的资源类型包括云服务器、云磁盘、云数据库、负载均衡、对象存储、专有网络、安全组等；
> - [x] 漏洞扫描：可配置攻击目标，零误报，包含了爬虫、通用漏洞检测（如 SQL 注入、XSS 检测）、指纹信息收集（如 Web 服务、语言框架等）、专用漏洞检测等；

![功能架构](./frontend/src/assets/img/readme/functional-architecture.png)

## UI 界面展示

![UI 界面展示](./frontend/src/assets/img/readme/dashboard.gif)

## 快速开始

仅需两步快速安装 RiskScanner：

1.  准备一台不小于 4 G 内存的 64 位 Linux 主机；
2.  以 root 用户执行如下命令一键安装 RiskScanner。

```sh
curl -sSL https://github.com/riskscanner/riskscanner/releases/latest/download/quick_start.sh | sh
```

## 帮助文档和演示视频

> [帮助文档](https://docs.riskscanner.io/)

> [演示视频](https://www.bilibili.com/video/BV12p4y1b7Ud)

## 微信群

<img src="./frontend/src/assets/img/readme/wechat-group.png" width="156" height="156"/>

## 技术栈

- 前端：[Vue.js](https://vuejs.org/)
- 后端：[Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- 云平台扫描引擎：[Cloud Custodian](https://github.com/cloud-custodian/cloud-custodian)
- 漏洞扫描引擎：[Nuclei](https://github.com/projectdiscovery/nuclei)
- 数据库：[MySQL](https://www.mysql.com/)
- 基础设施：[Docker](https://www.docker.com/)

## License & Copyright

Copyright (c) 2014-2021 飞致云 FIT2CLOUD, All rights reserved.

Licensed under The GNU General Public License version 2 (GPLv2) (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

https://www.gnu.org/licenses/gpl-2.0.html

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
