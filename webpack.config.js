const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    mode: 'production',
    entry: './src-js/index.js',
    // devtool: 'source-map',
    output: {
        filename: 'index.bundle.js'
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: "[name].css",
            chunkFilename: "[id].css"
        })
    ],
    module: {
        rules: [
            {
                test: /\.less$/,
                use: [
                    {
                        loader: 'style-loader'
                    },
                    {
                        loader: 'css-loader',
                    },
                    {
                        loader: 'less-loader',
                        options: {
                            lessOptions: {
                                modifyVars: {
                                    // 'primary-color': '#1DA57A',
                                    // 'link-color': '#1DA571',
                                    // 'border-radius-bae': '2px',
                                    'hack': `true; @import "default.less";`
                                },
                                javascriptEnabled: true,
                            }
                        }
                    }
                ]
            }

        ]
    }
};