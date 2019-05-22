module.exports = {
    entry: './src-js/index.js',
    output: {
        filename: 'index.bundle.js'
    },

    module: {
        rules: [
            {
                test: /\.less$/,
                use: [
                    {
                        loader: 'style-loader'
                    },
                    {
                        loader: 'css-loader'
                    },
                    {
                        loader: 'less-loader',
                        options: {
                            modifyVars: {
                                'primary-color': '#1DA57A',
                                'link-color': '#1DA571',
                                'border-radius-bae': '2px',

                            },
                            javascriptEnabled: true,
                        }
                    }
                ]
            }
        ]
    }
}