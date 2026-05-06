# CLAUDE.md

このファイルはリポジトリ内のコードを操作する際に Claude Code (claude.ai/code) へのガイダンスを提供します。

## プロジェクト概要

設備点検管理アプリ — 工場の設備点検履歴・期限を管理する Spring Boot REST API。`frontend/` に React/TypeScript フロントエンドが含まれています。

点検計画や期限については、check_plan_and_history.mdを参照。

## アーキテクチャ

**レイヤー構成**: Controller → Service (インターフェース + Impl) → Mapper (MyBatis) → MySQL

**パッケージ**: `com.example.equipment`

- `controller/` — REST エンドポイント。例外処理は各コントローラーの `@ExceptionHandler` で対応
- `service/` — ビジネスロジック。インターフェースと Impl クラスのペア構成
- `mapper/` — MyBatis マッパー（XML ではなく SQL アノテーションを使用）
- `entity/` — ドメインモデル（Equipment、Plan、History）
- `form/` — Bean Validation アノテーション付きのリクエスト DTO
- `exception/` — `ResourceNotFoundException`（404 にマッピング）

**設計上の注意点:**
- MyBatis の SQL はマッパーインターフェースにアノテーションで直接記述（XML ファイルなし）
- Lombok をゲッター・セッター・コンストラクター生成に全体的に使用
- `controller/` 内の `FindEquipmentResponse` は設備・計画・履歴を結合したクエリ結果の DTO
- CORS は `src/main/java/com/example/equipment/config/CorsConfig.java` でプロファイルごとに設定

## 環境プロファイル

`EQUIPMENT_ACTIVE_PROFILE` 環境変数で切り替え（デフォルト: `local`）。

- `application-local.properties` — MySQL は `localhost:3306`、CORS は `http://localhost:3000`
- `application-prod.properties` — AWS RDS MySQL、DB パスワードは `EQUIPMENT_DB_PASSWORD` 環境変数から取得、CORS は EC2 の IP

メール設定は `application.properties` で `EQUIPMENT_MAIL_USER` / `EQUIPMENT_MAIL_PASSWORD` 環境変数を使用。

## テスト

テスト実行前に Docker でテスト用 DB を起動する必要があります。

```bash
docker compose up -d
```

- **ユニットテスト** (Mockito): `*ServiceTest` — マッパー層をモック化
- **マッパーテスト** (DBRider): `*MapperTest` — 実 DB に接続し YAML フィクスチャを読み込み
- **統合テスト** (Spring Boot Test): `*IntegrationTest` — フルスタック

テストデータは DBRider フレームワークを使い、テストリソース内の YAML ファイルから読み込まれます。

## コマンド

### ローカルでのアプリ起動

以下の順番で起動する。

```bash
# 1. Docker 起動（MySQL）
docker compose up -d

# 2. Spring Boot 起動（ローカルプロファイル）
./gradlew bootRun

# 3. フロントエンド起動（別ターミナルで実行）
cd frontend && yarn start
```

### その他のコマンド

```bash
# 全テスト実行
./gradlew test

# 特定のテストクラスを実行
./gradlew test --tests "com.example.equipment.service.EquipmentServiceTest"

# 特定のテストメソッドを実行
./gradlew test --tests "com.example.equipment.service.EquipmentServiceTest.メソッド名"

# コードスタイルチェック
./gradlew checkstyleMain

# ビルド
./gradlew build
```

## コードスタイル

Checkstyle で Google Java Style Guide を適用（行の最大文字数: 100）。PR 提出前に `./gradlew checkstyleMain` を実行してください。CI でも自動実行され、違反は reviewdog で報告されます。

## CI

GitHub Actions (`.github/workflows/ci.yml`) が main へのプッシュと PR で実行されます。
フロー: Checkstyle → Docker DB 起動 → テスト → レポート公開 → Discord 通知
