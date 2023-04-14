<br />

> **Warning**
> # 此项目已停止维护，已迁移至 [CloudExplorer Lite](https://www.fit2cloud.com/cloudexplorer-lite/index.html) 的安全合规模块。
<br />
<br />
<h3 align="center">RiskScanner 开源多云安全合规扫描平台</h3>

<p align="center">
   <a href="https://www.codacy.com/gh/riskscanner/riskscanner/dashboardutm_source=github.com&amp;utm_medium=referral&amp;utm_content=riskscanner/riskscanner&amp;utm_campaign=Badge_Grade"><img src="https://app.codacy.com/project/badge/Grade/3331d2c045ae4d0ba1fd8fdd623186e7" alt="A"/></a>
  <a href="https://www.gnu.org/licenses/old-licenses/gpl-2.0"><img src="https://img.shields.io/github/license/riskscanner/riskscanner?color=%231890FF&style=flat-square" alt="License: GPL v2"></a>
  <a href="https://github.com/riskscanner/riskscanner/releases/latest"><img src="https://img.shields.io/github/v/release/riskscanner/riskscanner" alt=""></a>
  <a href="https://github.com/riskscanner/riskscanner"><img src="https://img.shields.io/github/stars/riskscanner/riskscanner?color=%231890FF&style=flat-square" alt=""></a>
  <a href="https://github.com/riskscanner/riskscanner/releases"><img src="https://img.shields.io/github/downloads/riskscanner/riskscanner/total" alt=""></a>
</p>
<hr />

RiskScanner 是开源的多云安全合规扫描平台，基于 Cloud Custodian、Prowler 和 Nuclei 引擎，实现对主流公(私)有云资源的安全合规扫描和漏洞扫描。

**功能优势**

> - [x] 等保 2.0 预检：符合等保 2.0 规范，覆盖安全审计、访问控制、入侵防范、网络架构和管理中心等各项检查；
> - [x] CIS 合规检查：符合 CIS 规范，检查和实时监控在云上的资源是否符合 CIS 要求；
> - [x] 漏洞扫描：基于漏洞规则库，通过扫描等手段对指定的网络设备及应用服务的安全脆弱性进行检测；
> - [x] 最佳实践建议：制定合规管控基线，为企业级用户提供最佳实践建议，持续提升合规水平。

RiskScanner 遵循 GPL v2 开源协议，使用 SpringBoot/Vue 进行开发，界面美观、用户体验好。RiskScanner支持的公有云包括阿里云、腾讯云、华为云、Amazon Web Services、Microsoft Azure、Google Cloud，支持的私有云包括 OpenStack、VMware vSphere。

**技术优势**

> - [x] 简单灵活的云平台扫描规则：扫描规则采用简单的 YAML 格式，简单易懂、并允许用户自定义规则；
> - [x] 支持多公(私)有云：支持的公有云包括阿里云、腾讯云、华为云、Amazon Web Services、Microsoft Azure、Google Cloud，支持的私有云包括 OpenStack、VMware vSphere；
> - [x] 支持多资源类型：支持的资源类型包括云服务器、云磁盘、云数据库、负载均衡、对象存储、专有网络、安全组等；
> - [x] 丰富全面的漏洞规则库：覆盖 OWASP TOP 10 的 Web 漏洞，例如：SQL 注入、跨站脚本攻击（XSS）、跨站请求伪造（CSRF）、弱密码等。

![功能架构](https://cdn0-download-offline-installer.fit2cloud.com/riskscanner/img/functional-architecture.png)

## UI 界面展示

![UI 界面展示](https://cdn0-download-offline-installer.fit2cloud.com/riskscanner/img/dashboard.gif)


## 技术栈

- 前端：[Vue.js](https://vuejs.org/)
- 后端：[Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- 云平台扫描引擎：[Cloud Custodian](https://github.com/cloud-custodian/cloud-custodian)
- AWS 扫描引擎：[Prowler](https://github.com/toniblyx/prowler)
- 漏洞扫描引擎：[Nuclei](https://github.com/projectdiscovery/nuclei)
- 数据库：[MySQL](https://www.mysql.com/)

## License & Copyright

Copyright (c) 2014-2023 飞致云 FIT2CLOUD, All rights reserved.

Licensed under The GNU General Public License version 2 (GPLv2) (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

https://www.gnu.org/licenses/gpl-2.0.html

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
